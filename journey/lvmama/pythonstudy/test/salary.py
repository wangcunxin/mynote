#!/usr/bin/python
salary=int(raw_input('how much is your salary:\n'))

shopping_list=['plane 1000000','car 100000','bike 300']
purchase_list=[]

while True:
	print '--- shopping list---'
	canBuy=0	
	product_list=[]
	price_list=[]	

	#display
	for product in shopping_list:
		name=product.split()[0]
		price=int(product.split()[1])
		print name,'\t',price
		
		price_list.append(price)
		product_list.append(name)		

		if salary >= price :
			canBuy=1
		else:
			continue
	
	#over
	if canBuy==0:
		print '\nyou can not to anything'
		print 'you have bought:',purchase_list	
		break

	#choice
	option=raw_input('\nwhat do u wanna buy:')

	if len(option)==0 :
		continue
	if option not in product_list:	
		print "sorry,we don't have %s on sale,try again" % option
		continue
	
	price_of_product=price_list[product_list.index(option)]
	print "the price of %s is %s" % (option,price_of_product)

	#add product
	if salary >= price_of_product:
		print 'add %s to purchase list' % option
		purchase_list.append(option)
		salary=salary-price_of_product
		print 'the money left:',salary
		
		keep_buy=raw_input('\ncontinue to buy?[y/n]')
		
		if keep_buy== 'y|Y' or len(keep_buy)== 0 :
			continue
		else:
			print 'you have bought %s' % purchase_list
			print 'the money left:',salary
			break
	else:
		print 'sorry,you only have %s , try to buy others' % salary
	
