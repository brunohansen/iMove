from decimal import *

getcontext().prec = 3

def cj(l, m):
    n = Decimal(0)
    for i in range(len(m)):
        n = n + m[i][l]
    return n

def nhd(m):
    k = Decimal(len(m))
    l = Decimal(len(m[0]))
    n = Decimal(0)
    for i in range(int(l)):
        c = cj(i, m)
        n = n + (c * (k - c))
    return (1 - ((2 / (l * k * (k - 1))) * n))

def zj(l, m):
    n = Decimal(0)
    for i in range(len(m)):
        n = n + (1 - m[i][l])
    return n

def nhdm(m):
    k = Decimal(len(m))
    l = Decimal(len(m[0]))
    n = Decimal(0)
    for i in range(int(l)):
        c = cj(i, m)
        z = zj(i, m)
        n = n + (c * (k - c)) + (Decimal('0.5') * (z * (z - 1)))
    return (1 - ((2 / (l * k * (k - 1))) * n))

def camc(m):
    k = Decimal(len(m))
    l = Decimal(len(m[0]))
    n = Decimal(0)
    for i in range(int(l)):
        c = cj(i, m)
        n = n + c
    return n / (k * l)

def cci(m):
    k = Decimal(len(m))
    l = Decimal(len(m[0]))
    n = Decimal(0)
    for i in range(int(k) - 1):
        for i2 in range(i + 1, int(k)):
            a = d = Decimal(0)
            for j in range(int(l)):
                c = m[i][j] + m[i2][j]
                if c == 2:
                    a = a + 1
                if c == 1:
                    d = d + 1
            n = n + (a / (a + d))
    return n / ((k * (k - 1)) / 2)

def pp(m):
    k = Decimal(len(m))
    l = Decimal(len(m[0]))
    n = Decimal(0)
    for j in range(int(l) - 1):
        for j2 in range(j + 1, int(l)):
            a = d = Decimal(0)
            for i in range(int(k)):
                c = m[i][j] + m[i][j2]
                if c == 2:
                    a = a + 1
                if c == 1:
                    d = d + 1
            n = n + (a / (a + d))
    return n / ((l * (l - 1)) / 2)

def im(m):
    k = Decimal(len(m))
    l = Decimal(len(m[0]))
    n = Decimal(0)
    for i in range(int(k) - 1):
        for i2 in range(i + 1, int(k)):
            for j in range(int(l)):
                c = m[i][j] + m[i2][j]
                if c == 2:
                    n = n + 1
                    break
    return n / ((k * (k - 1)) / 2)

def ip(m):
    k = Decimal(len(m))
    l = Decimal(len(m[0]))
    n = Decimal(0)
    for j in range(int(l) - 1):
        for j2 in range(j + 1, int(l)):
            for i in range(int(k)):
                c = m[i][j] + m[i][j2]
                if c == 2:
                    n = n + 1
                    break
    return n / ((l * (l - 1)) / 2)

def ic(m):
    r = ((cci(m) + pp(m)) / 2) * Decimal('0.5')
    i = ((im(m) + ip(m)) / 2) * Decimal('0.5')
    return r + i
    
def ic2(m):
	ic = Decimal(0)
	for i in range(len(m)):
		ic = ic + ic3(m, i)
	return ic / len(m)

def ic3(m, i2):
	cci = cci2(m, i2)
	im = im2(m, i2)
	pp = Decimal(0)
	ip = Decimal(0)
	p = Decimal(0)
	for j in range(len(m[i2])):
		if(m[i2][j] == 1):
			p = p + 1
			pp = pp + pp2(m, j)
			ip = ip + ip2(m, j)
	pp = pp / p
	ip = ip / p
	r = ((cci + pp) / 2) * Decimal('0.5')
	i = ((im + ip) / 2) * Decimal('0.5')
	return r + i

def cci2(m, i2):
	if (len(m) == 0):
		return Decimal(0)
	if (len(m[0]) == 0):
		return Decimal(0)
	k = Decimal(len(m))
	l = Decimal(len(m[0]))
	n = Decimal(0)
	for i in range(int(k)):
		if (i == i2):
			continue
		a = d = Decimal(0)
		for j in range(int(l)):
			c = m[i][j] + m[i2][j]
			if (c == 2):
				a = a + 1
			if (c == 1):
				d = d + 1
		n = n + (a / (a + d))
	return n / (k - 1)

def pp2(m, j2):
	if (len(m) == 0):
		return Decimal(0)
	if (len(m[0]) == 0):
		return Decimal(0)
	k = Decimal(len(m))
	l = Decimal(len(m[0]))
	n = Decimal(0)
	for j in range(int(l)):
		if (j == j2):
			continue
		a = d = Decimal(0)
		for i in range(int(k)):
			c = m[i][j] + m[i][j2]
			if (c == 2):
				a = a + 1
			if (c == 1):
				d = d + 1
		n = n + (a / (a + d))
	return n / (l - 1)

def im2(m, i2):
	if (len(m) == 0):
		return Decimal(0)
	if (len(m[0]) == 0):
		return Decimal(0)
	k = Decimal(len(m))
	l = Decimal(len(m[0]))
	n = Decimal(0)
	for i in range(int(k)):
		if (i == i2):
			continue
		for j in range(int(l)):
			c = m[i][j] + m[i2][j]
			if (c == 2):
				n = n + 1
				break
	return n / (k - 1)

def ip2(m, j2):
	if (len(m) == 0):
		return Decimal(0)
	if (len(m[0]) == 0):
		return Decimal(0)
	k = Decimal(len(m))
	l = Decimal(len(m[0]))
	n = Decimal(0)
	for j in range(int(l)):
		if (j == j2):
			continue
		for i in range(int(k)):
			c = m[i][j] + m[i][j2]
			if (c == 2):
				n = n + 1
				break
	return n / (l - 1)

a11 = [[1, 0], [0, 1]]
a12 = [[1, 0, 0], [0, 1, 0], [0, 0, 1]]

a21 = [[1, 0, 0, 0], [1, 1, 1, 1], [1, 1, 1, 1], [0, 0, 1, 0], [0, 0, 0, 1]]
a22 = [[0, 0, 1, 1], [1, 1, 0, 0], [0, 1, 1, 0], [1, 0, 0, 1], [0, 1, 1, 1]]

a31 = [[1, 0], [0, 1]]
a32 = [[1, 0, 0], [0, 1, 0], [0, 0, 1]]

a41 = [[1, 1, 1, 1], [1, 0, 0, 0], [1, 1, 1, 1], [0, 1, 1, 1]]
a42 = [[0, 0, 1, 0], [1, 0, 0, 0], [0, 0, 0, 1], [0, 1, 0, 0]]

vm1 = [[1, 0, 0, 0], [1, 0, 0, 0], [1, 0, 0, 0], [0, 1, 1, 1]]
vm2 = [[1, 0, 0, 0], [1, 0, 0, 0], [1, 1, 0, 0], [0, 1, 1, 1]]

a5a1 = [[1, 0, 1], [1, 1, 0], [0, 1, 1], [1, 0, 0]]
a5a2 = [[1, 0, 1], [1, 1, 0], [0, 1, 1], [1, 0, 0], [1, 0, 0]]

a5b1 = [[1, 0, 1], [1, 1, 0], [0, 1, 1], [1, 0, 0], [1, 0, 0], [1, 0, 0]]
a5b2 = [[1, 0, 1], [1, 1, 0], [0, 1, 1], [1, 0, 0], [1, 0, 0], [1, 0, 0], [1, 0, 0]]

meu1 = [[1, 1, 1, 0], [0, 1, 1, 1], [0, 1, 1, 0]]

def cresce():
	m1 = [[1, 0, 1], [1, 1, 0], [0, 1, 1], [1, 0, 0]]
	print(ic(m1))
	for i in range(1, 100):
		m1.append([1, 0, 0])
		print(ic(m1))

def cresce2():
	m1 = [[1, 1, 1, 0], [0, 1, 1, 1], [0, 1, 1, 0]]
	print(ic(m1))
	for i in range(1, 10):
		m1.append([0, 1, 1, 0])
		print(ic(m1))
