@ECHO OFF
ECHO insert parameter to initialize the server
set /p S=S - number of allowed S-threads: 
set /p C=C - size of the cache: 
set /p M=M - the least number of times a query has to be requested in order to be allowed to enter the cache: 
set /p L=L - to specify the range [1, L] from which missing replies will be drawn uniformly at random: 
set /p Y=Y - number of reader threads: 
ECHO Run server Save the log in Log-Server.txt
start "OS-Server" cmd /k "java -jar "ServerOS.jar" %S% %C% %M% %L% %Y% > Log-Server.txt"

set /p NC=S - number of clients to open: 
ECHO now run clients
for /l %%x in (1, 1, %NC%) do start cmd /k java -jar "ClientOS.jar" 100 101 ProbabilityFiles/%%x.txt
PAUSE