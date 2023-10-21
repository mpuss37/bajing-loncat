# bajing-locant
"bajing-loncat" is a program that allows users to quickly retrieve legal or illegal data, such as a bajing-loncat (check on Wikipedia) in stealing valuables. With a simple command line interface, the program offers high efficiency in data retrieval.
install it first :
pacman -S jdk-openjdk (based arch)
apt install jdk-openjdk (based debian)

https://download.oracle.com/java/17/latest/jdk-17_windows-x64_bin.exe (based windows)

next step :
git clone https://github.com/mpuss37/bajing-loncat.git
&& cd bajing-loncat/gradlew jar
check dir build/libs/file.jar
and running with this command
java -jar filename.jar [argument]
Congratulations, the program has been installed

Usage:
 balon [OPTIONS]...[VALUES]	
  -n, --normal ['query'] [amount] [numberPage]    Normal data fetch.
  -f, --free   ['query'] [amount] [numberPage]     Free data fetch.
  -o, --orient ['query'] [amount] [numberPage] [orientation]    Specific orientation data fetch.
  -c, --color  ['query'] [amount] [numberPage] [color]    Specific color data fetch.
  -r, --order  ['query'] [amount] [numberPage] [order]    Specific order data fetch.
  -a, --all    ['query'] [amount] [numberPage] [color] [order]    Perfect criteria data fetch.
  -h, --help    Display usage,options and help.
Example :
soon
