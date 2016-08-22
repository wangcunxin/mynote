# -*- coding: UTF-8 -*-

if __name__ == '__main__':
    c = 10
    while c > 0:
        print(c)
        c -= 1
    else:
        print("over")

    for letter in 'Python':  # 第一个实例
        print '当前字母 :', letter

    fruits = ['banana', 'apple', 'mango']
    for fruit in fruits:  # 第二个实例
        print '当前字母 :', fruit

    print "Good bye!"

    fruits = ['banana', 'apple',  'mango']
    for index in range(0,len(fruits),1):
       print '当前水果 :', fruits[index]

    print "Good bye!"