#include <stdio.h>

// 观察C代码转化成的ARM汇编代码
void if_code(int a) {
    if (a > 5) {
        printf("AAAAA");
    }
}

void ifelse_code(int a) {
    if (a > 5) {
        printf("AAAAA");
    } else if (a == 5) {
        printf("BBBBB");
    } else {
        printf("CCCCC");
    }
}

void switch_code(int a) {
    switch (a) {
        case 1:
            printf("AAAAA");
            break;
        case 2:
            printf("BBBBB");
            break;
        case 3:
            printf("CCCCC");
            break;
        case 4:
            printf("DDDDD");
            break;
        case 5:
            printf("EEEEE");
            return;
    }
}

void for_code(int a) {
    for (int i = 0; i < 5; ++i) {
        printf("AAAAA");
    }
}

void while_code(int a) {
    while (a > 0) {
        printf("AAAAA");
        a--;
    }
}

// 函数测试
void fun_test(int a) {
    if_code(a);
    ifelse_code(a);
    switch_code(a - 1);
    for_code(a);
    while_code(a);
}
