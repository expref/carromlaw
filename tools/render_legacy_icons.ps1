# Renders pre-API-26 launcher icons (PNG) from the same composition as the
# adaptive-icon vectors in app/src/main/res/drawable/ic_launcher_*.xml.
# Outputs ic_launcher.png and ic_launcher_round.png at 48/72/96/144/192 px
# into mipmap-{m,h,x,xx,xxx}hdpi. Re-run when the vector design changes.

Add-Type -AssemblyName System.Drawing

$root = Resolve-Path (Join-Path $PSScriptRoot '..\app\src\main\res')

$densities = [ordered]@{
    'mdpi'    = 48
    'hdpi'    = 72
    'xhdpi'   = 96
    'xxhdpi'  = 144
    'xxxhdpi' = 192
}

function Render-LauncherIcon {
    param(
        [int]$Size,
        [bool]$Round
    )

    $bmp = New-Object System.Drawing.Bitmap $Size, $Size, ([System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
    $g = [System.Drawing.Graphics]::FromImage($bmp)
    $g.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::AntiAlias
    $g.PixelOffsetMode = [System.Drawing.Drawing2D.PixelOffsetMode]::HighQuality
    $g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic

    # Map the 108-unit vector viewport onto the target bitmap.
    $g.ScaleTransform(($Size / 108.0), ($Size / 108.0))

    if ($Round) {
        $clip = New-Object System.Drawing.Drawing2D.GraphicsPath
        $clip.AddEllipse(0, 0, 108, 108)
        $g.SetClip($clip)
        $clip.Dispose()
    }

    # Background layer 1: linear gradient #0D3B66 -> #1F4E79 across the diagonal.
    $bgStart = [System.Drawing.Color]::FromArgb(255, 0x0D, 0x3B, 0x66)
    $bgEnd   = [System.Drawing.Color]::FromArgb(255, 0x1F, 0x4E, 0x79)
    $linBrush = New-Object System.Drawing.Drawing2D.LinearGradientBrush(
        (New-Object System.Drawing.PointF 0, 0),
        (New-Object System.Drawing.PointF 108, 108),
        $bgStart, $bgEnd)
    $g.FillRectangle($linBrush, (New-Object System.Drawing.RectangleF 0, 0, 108, 108))
    $linBrush.Dispose()

    # Background layer 2: radial glow at (54,54) r=48, alpha-fading to transparent.
    $glowPath = New-Object System.Drawing.Drawing2D.GraphicsPath
    $glowPath.AddEllipse(6, 6, 96, 96)
    $glowBrush = New-Object System.Drawing.Drawing2D.PathGradientBrush $glowPath
    $glowBrush.CenterPoint = New-Object System.Drawing.PointF 54, 54
    $glowBrush.CenterColor = [System.Drawing.Color]::FromArgb(0x33, 0x2C, 0x5C, 0x8C)
    $glowBrush.SurroundColors = @([System.Drawing.Color]::FromArgb(0x00, 0x0D, 0x3B, 0x66))
    $g.FillPath($glowBrush, $glowPath)
    $glowBrush.Dispose()
    $glowPath.Dispose()

    # Outer gold ring r=28, stroke 1.6, #FFD54F a=0.85.
    $goldPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(217, 0xFF, 0xD5, 0x4F)), 1.6
    $g.DrawEllipse($goldPen, 26, 26, 56, 56)
    $goldPen.Dispose()

    # Inner soft white ring r=22, stroke 0.8, #FFFFFF a=0.40.
    $whitePen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(102, 0xFF, 0xFF, 0xFF)), 0.8
    $g.DrawEllipse($whitePen, 32, 32, 44, 44)
    $whitePen.Dispose()

    # Queen disc r=15 at (54,54). The radial gradient is anchored at (50,50) r=18,
    # so we paint a solid base in the surround colour first, then clip to the disc
    # and overlay the gradient. This gives the asymmetric highlight without leaving
    # uncovered pixels where the disc extends past the gradient path.
    $discPath = New-Object System.Drawing.Drawing2D.GraphicsPath
    $discPath.AddEllipse(39, 39, 30, 30)

    $baseBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(255, 0xB7, 0x1C, 0x1C))
    $g.FillPath($baseBrush, $discPath)
    $baseBrush.Dispose()

    $gradPath = New-Object System.Drawing.Drawing2D.GraphicsPath
    $gradPath.AddEllipse(32, 32, 36, 36)
    $queenBrush = New-Object System.Drawing.Drawing2D.PathGradientBrush $gradPath
    $queenBrush.CenterPoint = New-Object System.Drawing.PointF 50, 50
    $queenBrush.CenterColor = [System.Drawing.Color]::FromArgb(255, 0xF5, 0x5F, 0x5F)
    $queenBrush.SurroundColors = @([System.Drawing.Color]::FromArgb(255, 0xB7, 0x1C, 0x1C))

    $savedClip = $g.Clip.Clone()
    $g.SetClip($discPath, [System.Drawing.Drawing2D.CombineMode]::Intersect)
    $g.FillPath($queenBrush, $gradPath)
    $g.Clip = $savedClip
    $savedClip.Dispose()

    $queenBrush.Dispose()
    $gradPath.Dispose()

    # Queen highlight: white disc r=4 at (50,50) a=0.35.
    $hlBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(89, 0xFF, 0xFF, 0xFF))
    $g.FillEllipse($hlBrush, 46, 46, 8, 8)
    $hlBrush.Dispose()

    # Queen rim: dark red ring on the disc edge, stroke 0.8, #7F0000 a=0.55.
    $rimPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(140, 0x7F, 0x00, 0x00)), 0.8
    $g.DrawEllipse($rimPen, 39, 39, 30, 30)
    $rimPen.Dispose()
    $discPath.Dispose()

    # Four pocket dots r=3 at (37,37), (71,37), (37,71), (71,71), white a=0.92.
    $dotBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(235, 0xFF, 0xFF, 0xFF))
    foreach ($pt in @(@(37, 37), @(71, 37), @(37, 71), @(71, 71))) {
        $g.FillEllipse($dotBrush, ($pt[0] - 3), ($pt[1] - 3), 6, 6)
    }
    $dotBrush.Dispose()

    $g.Dispose()
    return $bmp
}

foreach ($entry in $densities.GetEnumerator()) {
    $density = $entry.Key
    $size = $entry.Value
    $dir = Join-Path $root "mipmap-$density"

    $square = Render-LauncherIcon -Size $size -Round:$false
    $square.Save((Join-Path $dir 'ic_launcher.png'), [System.Drawing.Imaging.ImageFormat]::Png)
    $square.Dispose()

    $round = Render-LauncherIcon -Size $size -Round:$true
    $round.Save((Join-Path $dir 'ic_launcher_round.png'), [System.Drawing.Imaging.ImageFormat]::Png)
    $round.Dispose()

    Write-Output "wrote mipmap-$density at ${size}x${size}"
}
