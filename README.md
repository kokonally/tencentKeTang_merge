#  腾讯课堂m3u8转mp4
# 1.项目目的：

使用大佬的项目：[**tencentKeTang**](https://github.com/HarryWang29/tencentKeTang)
进行下载腾讯课堂视频时，很多无法合并，所以直接就写了这个Java程序进行处理

# 2.使用方法：

直接下载Main.java文件，放在[**tencentKeTang**](https://github.com/HarryWang29/tencentKeTang)
项目的根路径下，通过javac Main.java编译，通过java Main运行。或者直接下载Main.class直接java Main运行。

# 3.注意事项：
1.下载路径与 **ffmpeg** 默认都是在
**[tencentKeTang](https://github.com/HarryWang29/tencentKeTang)**
项目的跟路径下，如果更改请运行java Main 参数1 参数2。参数1为源路径，参数2为ffmpeg路径，例：java Main D:/tx_download D:/ffmpeg

2.只适用于windows系统

3.运行时需要安装好ffmpeg，编译时需要安装好maven

