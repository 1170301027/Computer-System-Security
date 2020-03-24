#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <unistd.h>
#undef _POSIX_SOURCE
#include <sys/capability.h>
#include <signal.h>

void getpidbyname(char *, char *);

int main(){
    struct __user_cap_header_struct cap_header_data;
    cap_user_header_t cap_header = &cap_header_data;
    struct __user_cap_data_struct cap_data_data;
    cap_user_data_t cap_data = &cap_data_data;
    cap_header->pid = getpid();
    cap_header->version = _LINUX_CAPABILITY_VERSION_1;
    if (capget(cap_header, cap_data) < 0) {
         perror("Failed capget");
         exit(1);
    }
    

    printf("%x\n", cap_data->effective);
    for (int i = 0; i < 32; i++) {
        int mask = 1;
        char pid[10] ={0};
        char * process = "capsettest";
        if (cap_data->effective & (mask << i)) {
            switch(i) {
                case 0:
                    break;
                case 5:
                    getpidbyname(process, pid);
                    puts(pid);
                    kill(atoi(pid), SIGKILL);
                    printf("kill sucessfully");
                    return 0;
                    //break;
                default:
                    break;
            }
        }
    }
    printf("the process does not have the cap\n");


}



void getpidbyname(char * process, char *pid){
    char *command1 = "ps -A | grep ";
    char *command2 = " | awk \'{print $1}\'";
    char command[128] = {0};
    strcat(command, command1);
    strcat(command, process);
    strcat(command, command2);
    FILE *fp = popen(command, "r");
    while(NULL != fgets(pid, 10, fp)){
        pid[strlen(pid)-1] = '\0';
        printf("PIDL %s\n", pid);
    } 
    pclose(fp);










}


