#!/usr/bin/python

print 'please input hk$ or us$ to exchange rmb'

rmb = int(raw_input('input:'))

us=rmb/6.4
hk=rmb/0.84

print 'you can exchange:',hk,'hk$',us,'us$'
