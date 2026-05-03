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

function Add-RoundedRectPath {
    param(
        [System.Drawing.Drawing2D.GraphicsPath]$Path,
        [single]$X, [single]$Y, [single]$W, [single]$H, [single]$R
    )
    $d = 2 * $R
    # AddArc(x, y, w, h, startAngle, sweepAngle) — angles are clockwise from 3 o'clock.
    $Path.AddArc($X + $W - $d, $Y,                $d, $d, 270, 90) | Out-Null  # top-right
    $Path.AddArc($X + $W - $d, $Y + $H - $d,      $d, $d,   0, 90) | Out-Null  # bottom-right
    $Path.AddArc($X,           $Y + $H - $d,      $d, $d,  90, 90) | Out-Null  # bottom-left
    $Path.AddArc($X,           $Y,                $d, $d, 180, 90) | Out-Null  # top-left
    $Path.CloseFigure()
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

    $g.ScaleTransform(($Size / 108.0), ($Size / 108.0))

    if ($Round) {
        $clip = New-Object System.Drawing.Drawing2D.GraphicsPath
        $clip.AddEllipse(0, 0, 108, 108)
        $g.SetClip($clip)
        $clip.Dispose()
    }

    # ----- Background -----
    # Linear wood gradient: caramel #C8965C -> walnut #5C3317 across the diagonal.
    $bgStart = [System.Drawing.Color]::FromArgb(255, 0xC8, 0x96, 0x5C)
    $bgEnd   = [System.Drawing.Color]::FromArgb(255, 0x5C, 0x33, 0x17)
    $linBrush = New-Object System.Drawing.Drawing2D.LinearGradientBrush(
        (New-Object System.Drawing.PointF 0, 0),
        (New-Object System.Drawing.PointF 108, 108),
        $bgStart, $bgEnd)
    $g.FillRectangle($linBrush, (New-Object System.Drawing.RectangleF 0, 0, 108, 108))
    $linBrush.Dispose()

    # Warm radial highlight at (54,54) r=50, alpha-fading to transparent.
    $glowPath = New-Object System.Drawing.Drawing2D.GraphicsPath
    $glowPath.AddEllipse(4, 4, 100, 100)
    $glowBrush = New-Object System.Drawing.Drawing2D.PathGradientBrush $glowPath
    $glowBrush.CenterPoint = New-Object System.Drawing.PointF 54, 54
    $glowBrush.CenterColor = [System.Drawing.Color]::FromArgb(0x44, 0xE5, 0xC1, 0x8A)
    $glowBrush.SurroundColors = @([System.Drawing.Color]::FromArgb(0x00, 0xC8, 0x96, 0x5C))
    $g.FillPath($glowBrush, $glowPath)
    $glowBrush.Dispose()
    $glowPath.Dispose()

    # ----- Book body (rounded rect, x:26-82, y:22-86, r=4) -----
    $bookPath = New-Object System.Drawing.Drawing2D.GraphicsPath
    Add-RoundedRectPath -Path $bookPath -X 26 -Y 22 -W 56 -H 64 -R 4

    $bookBrush = New-Object System.Drawing.Drawing2D.LinearGradientBrush(
        (New-Object System.Drawing.PointF 26, 22),
        (New-Object System.Drawing.PointF 82, 86),
        [System.Drawing.Color]::FromArgb(255, 0x5A, 0x38, 0x23),
        [System.Drawing.Color]::FromArgb(255, 0x3A, 0x20, 0x14))
    $g.FillPath($bookBrush, $bookPath)
    $bookBrush.Dispose()

    # ----- Spine band (left 5 units of the book, sharing the rounded left corners) -----
    # Constructed as a closed path manually, since only the two left corners are rounded.
    $spinePath = New-Object System.Drawing.Drawing2D.GraphicsPath
    $spinePath.AddLine([single]31, [single]22, [single]31, [single]86)
    $spinePath.AddLine([single]31, [single]86, [single]30, [single]86)
    $spinePath.AddArc([single]26, [single]82, [single]8, [single]8, 90, 90)   # bottom-left
    $spinePath.AddLine([single]26, [single]82, [single]26, [single]26)
    $spinePath.AddArc([single]26, [single]22, [single]8, [single]8, 180, 90)  # top-left
    $spinePath.CloseFigure()

    $spineBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(255, 0x2D, 0x18, 0x10))
    $g.FillPath($spineBrush, $spinePath)
    $spineBrush.Dispose()
    $spinePath.Dispose()

    # Spine highlight line at x=31.
    $spineHlPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(140, 0x6A, 0x42, 0x26)), 0.4
    $g.DrawLine($spineHlPen, [single]31, [single]24, [single]31, [single]84)
    $spineHlPen.Dispose()

    # ----- Inner cover border (rounded rect outline, gold) -----
    $borderPath = New-Object System.Drawing.Drawing2D.GraphicsPath
    Add-RoundedRectPath -Path $borderPath -X 32 -Y 28 -W 44 -H 52 -R 2
    $borderPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(191, 0xD4, 0xAF, 0x37)), 0.6
    $g.DrawPath($borderPen, $borderPath)
    $borderPen.Dispose()
    $borderPath.Dispose()

    # ----- Carrom emblem -----
    # Outer gold ring at (54,48) r=12.
    $emblemPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(230, 0xD4, 0xAF, 0x37)), 1.4
    $g.DrawEllipse($emblemPen, 42, 36, 24, 24)
    $emblemPen.Dispose()

    # Soft inner ring at r=10.
    $innerRingPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(89, 0xFF, 0xFF, 0xFF)), 0.4
    $g.DrawEllipse($innerRingPen, 44, 38, 20, 20)
    $innerRingPen.Dispose()

    # Queen disc base at (54,48) r=7.
    $discPath = New-Object System.Drawing.Drawing2D.GraphicsPath
    $discPath.AddEllipse(47, 41, 14, 14)

    $baseBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(255, 0xB7, 0x1C, 0x1C))
    $g.FillPath($baseBrush, $discPath)
    $baseBrush.Dispose()

    # Queen radial gradient overlay anchored at (52,46) r=9.
    $gradPath = New-Object System.Drawing.Drawing2D.GraphicsPath
    $gradPath.AddEllipse(43, 37, 18, 18)
    $queenBrush = New-Object System.Drawing.Drawing2D.PathGradientBrush $gradPath
    $queenBrush.CenterPoint = New-Object System.Drawing.PointF 52, 46
    $queenBrush.CenterColor = [System.Drawing.Color]::FromArgb(255, 0xF5, 0x5F, 0x5F)
    $queenBrush.SurroundColors = @([System.Drawing.Color]::FromArgb(255, 0xB7, 0x1C, 0x1C))

    $savedClip = $g.Clip.Clone()
    $g.SetClip($discPath, [System.Drawing.Drawing2D.CombineMode]::Intersect)
    $g.FillPath($queenBrush, $gradPath)
    $g.Clip = $savedClip
    $savedClip.Dispose()

    $queenBrush.Dispose()
    $gradPath.Dispose()

    # Queen rim ring.
    $rimPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(140, 0x5C, 0x0A, 0x0A)), 0.4
    $g.DrawEllipse($rimPen, 47, 41, 14, 14)
    $rimPen.Dispose()
    $discPath.Dispose()

    # Queen highlight blob at (52,46) r=1.8.
    $hlBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(102, 0xFF, 0xFF, 0xFF))
    $g.FillEllipse($hlBrush, 50.2, 44.2, 3.6, 3.6)
    $hlBrush.Dispose()

    # ----- Embossed title bar (two short gold lines) -----
    $titlePen1 = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(153, 0xD4, 0xAF, 0x37)), 0.6
    $g.DrawLine($titlePen1, [single]42, [single]68, [single]66, [single]68)
    $titlePen1.Dispose()

    $titlePen2 = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(140, 0xD4, 0xAF, 0x37)), 0.5
    $g.DrawLine($titlePen2, [single]42, [single]72, [single]66, [single]72)
    $titlePen2.Dispose()

    # ----- Corner-tick markers (four small gold dots) -----
    $tickBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(217, 0xD4, 0xAF, 0x37))
    foreach ($pt in @(@(35, 31), @(73, 31), @(35, 77), @(73, 77))) {
        $g.FillEllipse($tickBrush, ($pt[0] - 0.9), ($pt[1] - 0.9), 1.8, 1.8)
    }
    $tickBrush.Dispose()

    $bookPath.Dispose()
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
