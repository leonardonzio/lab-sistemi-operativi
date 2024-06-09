#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>
#include <string.h>
#include <sys/wait.h>
#include <ctype.h>


int main(int argc, char** argv){

    // es2(0) file_in(1) car(2) N1(3) N2(4)  

    if (argc != 5){
        perror("errore argomenti:: es2 file_in car N1 N2\n");
        exit(-1); 
    }

    int N1 = atoi(argv[3]);
    int N2 = atoi(argv[4]);
    if (N1 <= 0 || N2 <= 0)
        perror("N1 e N2 devono essere interi positivi\n");
    

    int p2;
    int p1 = fork();
    if (!p1){
        p2 = fork();
        if (!p2){
            // p2




        } else if (p2 < 0){
            perror("errore fork p2!!!");
            exit(-3);
        }
        // p1



    }else if (p1 < 0){
        perror("errore fork p1!!!");
        exit(-2);
    }
    // p0



    









}






