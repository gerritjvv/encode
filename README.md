# Encode

Simple fast library that compress, encrypt and encode data, for session storage and other use-cases

[![Maven Central](https://img.shields.io/maven-central/v/com.github.gerritjvv/encode-core.svg)](https://search.maven.org/artifact/com.github.gerritjvv/encode-core/)

[![Javadocs](https://javadoc.io/badge/com.github.gerritjvv/encode-core.svg)](https://javadoc.io/doc/com.github.gerritjvv/encode-core)


## Releases


```xml
<dependency>
  <groupId>com.github.gerritjvv</groupId>
  <artifactId>encode-core</artifactId>
  <version>LATEST</version>
</dependency>
```

For the latest version see:  

https://search.maven.org/artifact/com.github.gerritjvv/encode-core/


## Usage


*Encode with Kryo:*  

```java
Map rawObj = buildRawObject();

byte[] bts = KryoEncoder.DEFAULT.encodeObject(rawObj);

Map decodedObj = KryoEncoder.DEFAULT.decodeObject(HashMap.class, bts);

```

*Debug Kryo*

Kryo can be tricky, and each concrete class must have a serializer registered.  
When you get strange errors, set the trace log. 

```java
KryoEncoder.setTraceLog();

//reset
KryoEncoder.setInfoLog();

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

## More examples:

See: [Tests](https://github.com/gerritjvv/encode/tree/master/encode-core/src/test/java/encode)

## Performance

See: https://github.com/gerritjvv/encode/tree/master/encode-perf


# License

https://www.apache.org/licenses/LICENSE-2.0

# Contributors

Contributions PRs and suggestions are always welcome.

Please ping me directly in the "issues" on "gerritjvv" or send me an email at gerritjvv@gmail.com, this way
the issues/pull-requests won't just linger if github notifications doens't work.

## Guide on publishing to maven central

https://dzone.com/articles/publish-your-artifacts-to-maven-central

### Release Process:

Follow:

https://www.rainerhahnekamp.com/en/publishing-a-java-library-to-maven-central/


Repository staging is deployed to https://oss.sonatype.org

