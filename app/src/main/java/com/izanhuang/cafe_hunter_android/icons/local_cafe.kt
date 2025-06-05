import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val undefined: ImageVector
	get() {
		if (_undefined != null) {
			return _undefined!!
		}
		_undefined = ImageVector.Builder(
            name = "Local_cafe",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
			path(
    			fill = SolidColor(Color.Black),
    			fillAlpha = 1.0f,
    			stroke = null,
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 1.0f,
    			strokeLineCap = StrokeCap.Butt,
    			strokeLineJoin = StrokeJoin.Miter,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(160f, 840f)
				verticalLineToRelative(-80f)
				horizontalLineToRelative(640f)
				verticalLineToRelative(80f)
				close()
				moveToRelative(160f, -160f)
				quadToRelative(-66f, 0f, -113f, -47f)
				reflectiveQuadToRelative(-47f, -113f)
				verticalLineToRelative(-400f)
				horizontalLineToRelative(640f)
				quadToRelative(33f, 0f, 56.5f, 23.5f)
				reflectiveQuadTo(880f, 200f)
				verticalLineToRelative(120f)
				quadToRelative(0f, 33f, -23.5f, 56.5f)
				reflectiveQuadTo(800f, 400f)
				horizontalLineToRelative(-80f)
				verticalLineToRelative(120f)
				quadToRelative(0f, 66f, -47f, 113f)
				reflectiveQuadToRelative(-113f, 47f)
				close()
				moveToRelative(0f, -80f)
				horizontalLineToRelative(240f)
				quadToRelative(33f, 0f, 56.5f, -23.5f)
				reflectiveQuadTo(640f, 520f)
				verticalLineToRelative(-320f)
				horizontalLineTo(240f)
				verticalLineToRelative(320f)
				quadToRelative(0f, 33f, 23.5f, 56.5f)
				reflectiveQuadTo(320f, 600f)
				moveToRelative(400f, -280f)
				horizontalLineToRelative(80f)
				verticalLineToRelative(-120f)
				horizontalLineToRelative(-80f)
				close()
				moveTo(320f, 600f)
				horizontalLineToRelative(-80f)
				horizontalLineToRelative(400f)
				close()
			}
		}.build()
		return _undefined!!
	}

private var _undefined: ImageVector? = null
