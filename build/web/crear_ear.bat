set nombre=%1

if not "%nombre%" == "" goto :crear_archivo

set /P nombre="Ingrese el nombre de la aplicacion: "

:crear_archivo
jar -cvfM "%nombre%"%1.war *
mkdir META-INF
copy d:\J\META-INF META-INF
jar -cvfM app_"%nombre%"%1.ear "%nombre%"%1.war META-INF
:rmdir /S /Q META-INF
set nombre=