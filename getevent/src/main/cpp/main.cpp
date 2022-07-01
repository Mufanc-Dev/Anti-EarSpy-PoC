#include <jni.h>

#include <cstdio>
#include <cstdlib>
#include <regex>
#include <unordered_map>
#include <dirent.h>
#include <android/log.h>
#include <linux/input.h>
#include <sys/epoll.h>
#include <sys/fcntl.h>
#include <sys/unistd.h>

#define LOGW(...) ((void)__android_log_print(ANDROID_LOG_WARN, "killer.queen", __VA_ARGS__))

struct label {
    const char *name;
    int value;
};

#define LABEL(constant) { #constant, constant }
#define LABEL_END { NULL, -1 }

#include "labels.h"

static const char *get_label(const label *labels, int value) {
    while(labels->name && value != labels->value) {
        labels++;
    }
    return labels->name;
}

const char *dev_path = "/dev/input";
int epfd = -1;


static void init() {
    // open directory of input devices
    DIR *dp = opendir(dev_path);
    if (dp == nullptr) {
        perror("opendir");
        exit(1);
    }

    // create epoll file descriptor
    epfd = epoll_create(1);
    if (epfd == -1) {
        perror("epoll_create");
        exit(1);
    }

    dirent *dir;
    while ((dir = readdir(dp)) != nullptr) {
        static auto pattern = std::regex("event\\d+");
        if (std::regex_match(dir->d_name, pattern)) {
            char path[256];
            sprintf(path, "%s/%s", dev_path, dir->d_name);
            int fd = open(path, O_RDONLY);

            epoll_event event {
                .events = EPOLLIN | EPOLLET,
                .data = {
                    .fd = fd
                }
            };

            epoll_ctl(epfd, EPOLL_CTL_ADD, fd, &event);
        }
    }
}


void print_event(int type, int code, int value) {
    const char *type_label, *code_label, *value_label = nullptr;
    type_label = get_label(ev_labels, type);
    switch (type) {
        case EV_SYN:
            code_label = get_label(syn_labels, code);
            break;
        case EV_KEY:
            code_label = get_label(key_labels, code);
            value_label = get_label(val_labels, value);
            break;
        case EV_ABS:
            code_label = get_label(abs_labels, code);
            break;
        default:
            return;
    }
    if (type_label)
        printf("%-12.12s", type_label);
    else
        printf("%04x        ", type);
    if (code_label)
        printf(" %-20.20s", code_label);
    else
        printf(" %04x                ", code);
    if (value_label)
        printf(" %-20.20s", value_label);
    else
        printf(" %08x            ", value);
    printf("\n");
}


JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *, void *) {
    init();
    return JNI_VERSION_1_6;
}


extern "C" JNIEXPORT void JNICALL
Java_anti_earspy_poc_getevent_GetEvent_waitFor(JNIEnv *env, jobject thiz) {
    epoll_event event { };
    epoll_wait(epfd, &event, 1, -1);
    int fd = event.data.fd;
    input_event input { };
    read(fd, &input, sizeof(input));
    print_event(input.type, input.code, input.value);
}