
# Hashids.java

A small Java class to generate YouTube-like hashes from one or many numbers.

Ported from javascript [hashids.js](https://github.com/ivanakimov/hashids.js) by [Ivan Akimov](https://github.com/ivanakimov)

## What is it?

hashids (Hash ID's) creates short, unique, decryptable hashes from unsigned (long) integers.

It was designed for websites to use in URL shortening, tracking stuff, or making pages private (or at least unguessable).

This algorithm tries to satisfy the following requirements:

1. Hashes must be unique and decryptable.
2. They should be able to contain more than one integer (so you can use them in complex or clustered systems).
3. You should be able to specify minimum hash length.
4. Hashes should not contain basic English curse words (since they are meant to appear in public places - like the URL).

Instead of showing items as `1`, `2`, or `3`, you could show them as `U6dc`, `u87U`, and `HMou`.
You don't have to store these hashes in the database, but can encrypt + decrypt on the fly.

All (long) integers need to be greater than or equal to zero.

## Usage

#### Import the package

```java
import HashidsJava;
```

#### Encrypting one number

You can pass a unique salt value so your hashes differ from everyone else's. I use "this is my salt" as an example.

```java

Hashids hashids = new Hashids("this is my salt");
String hash = hashids.encrypt(12345);
```

`hash` is now going to be:
	
	ryBo

#### Decrypting

Notice during decryption, same salt value is used:

```java

Hashids hashids = new Hashids("this is my salt");
long[] numbers = hashids.decrypt("ryBo");
```

`numbers` is now going to be:
	
	[ 12345 ]

#### Decrypting with different salt

Decryption will not work if salt is changed:

```java

Hashids hashids = new Hashids("this is my pepper");
long[] numbers = hashids.decrypt("ryBo");
```

`numbers` is now going to be:
	
	[]
	
#### Encrypting several numbers

```java

Hashids hashids = new Hashids("this is my salt");
String hash = hashids.encrypt(683, 94108, 123, 5);
```

`hash` is now going to be:
	
	zBphL54nuMyu5
	
#### Decrypting is done the same way

```java

Hashids hashids = new Hashids("this is my salt");
long[] numbers = hashids.decrypt("zBphL54nuMyu5");
```

`numbers` is now going to be:
	
	[ 683, 94108, 123, 5 ]
	
#### Encrypting and specifying minimum hash length

Here we encrypt integer 1, and set the minimum hash length to **8** (by default it's **0** -- meaning hashes will be the shortest possible length).

```java

Hashids hashids = new Hashids("this is my salt", 8);
String hash = hashids.encrypt(1);
```

`hash` is now going to be:
	
	b9iLXiAa
	
#### Decrypting

```java

Hashids hashids = new Hashids("this is my salt", 8);
long[] numbers = hashids.decrypt("b9iLXiAa");
```

`numbers` is now going to be:
	
	[ 1 ]
	
#### Specifying custom hash alphabet

Here we set the alphabet to consist of only four letters: "abcd"

```java

Hashids hashids = new Hashids("this is my salt", 0, "abcd");
String hash = hashids.encrypt(1, 2, 3, 4, 5);
```

`hash` is now going to be:
	
	adcdacddcdaacdad
	
## Randomness

The primary purpose of hashids is to obfuscate ids. It's not meant or tested to be used for security purposes or compression.
Having said that, this algorithm does try to make these hashes unguessable and unpredictable:

#### Repeating numbers

```java

Hashids hashids = new Hashids("this is my salt");
String hash = hashids.encrypt(5, 5, 5, 5);
```

You don't see any repeating patterns that might show there's 4 identical numbers in the hash:

	GLh5SMs9

Same with incremented numbers:

```java

Hashids hashids = new Hashids("this is my salt");
String hash = hashids.encrypt(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
```

`hash` will be :
	
	zEUzfySGIpuyhpF6HaC7
	
### Incrementing number hashes:

```java

Hashids hashids = new Hashids("this is my salt");
String hash1 = hashids.encrypt(1); /* LX */
String hash2 = hashids.encrypt(2); /* ed */
String hash3 = hashids.encrypt(3); /* o9 */
String hash4 = hashids.encrypt(4); /* 4n */
String hash5 = hashids.encrypt(5); /* a5 */
```

## Bad hashes

I wrote this class with the intent of placing these hashes in visible places - like the URL. If I create a unique hash for each user, it would be unfortunate if the hash ended up accidentally being a bad word. Imagine auto-creating a URL with hash for your user that looks like this - `http://example.com/user/a**hole`

Therefore, this algorithm tries to avoid generating most common English curse words with the default alphabet. This is done by never placing the following letters next to each other:
	
	c, C, s, S, f, F, h, H, u, U, i, I, t, T
	
## Contact

Follow me [@IvanAkimov](http://twitter.com/ivanakimov)

Or [http://ivanakimov.com](http://ivanakimov.com)

## License

MIT License. See the `LICENSE` file.