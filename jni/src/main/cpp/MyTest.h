
#ifndef HELLONATIVE_MYTEST_H
#define HELLONATIVE_MYTEST_H

int sum_three_and_mul(int a, int b, int c, int d, int e, int f);

class Point {
private:
    int x, y;
public:
    Point(int xx = 0, int yy = 0) : x(xx), y(yy) {};

    ~Point() {};

    int getX() const { return x; };

    int getY() const { return y; };
};

int number = 1000;
Point *point = new Point(10, 10);

void if_code(int);
void ifelse_code(int);
void switch_code(int);
void for_code(int);
void while_code(int);
void fun_test(int);

#endif //HELLONATIVE_MYTEST_H