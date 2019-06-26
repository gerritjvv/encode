# Encode

Simple fast library that compress, encrypt and encode data, for session storage and other use-cases


## Usage


*Encode with Kryo:*  

```java
Map rawObj = buildRawObject();

byte[] bts = KryoEncoder.DEFAULT.encodeObject(rawObj);

Map decodedObj = KryoEncoder.DEFAULT.decodeObject(HashMap.class, bts);

```



*Encode, Encrypt with Kryo and AES GCM:*  


```java
Key.ExpandedKey key = Key.KeySize.AES_128.genKeysHmacSha();

Encoder encoder = CryptoEncoder.getGCMInstance(key, KryoEncoder.DEFAULT);

Map rawObj = buildRawObject();

byte[] bts = encoder.encodeObject(rawObj);

Map decodedObj = encoder.decodeObject(HashMap.class, bts);

```


*Encode, Compress, Encrypt with Kryo, Lz4 and AES GCM:*  


```java

Key.ExpandedKey key = Key.KeySize.AES_256.genKeysHmacSha();

Encoder encoder = CryptoEncoder.getCBCHmacInstance(key, Lz4Encoder.getEncoder(KryoEncoder.DEFAULT));

Map rawObj = buildRawObject();

byte[] bts = encoder.encodeObject(rawObj);


Map decodedObj = encoder.decodeObject(HashMap.class, bts);

```