# Privacy Violation: Heap Inspection

I have written a detailed blog for Privacy Violation: Heap Inspection, you can check it [here](https://sahildari.medium.com/sast-series-part-2-ecbaca2b9c97)

__Privacy Violation: Heap Inspection__ is a source code security issue that occurs mostly in C#, Java, and Swift applications. Strings are immutable in these languages, meaning that if they are used to store sensitive information such as passwords, credit card numbers, secrets, or tokens, these values will remain in memory until the JVM garbage collector (in Java) or ARC (Automatic Reference Counting) (in Swift) removes them.

There is no guarantee as to when garbage collection will take place. In the event of an application crash, a memory dump might reveal sensitive data, making it accessible to anyone inspecting the heap before garbage collection occurs.

You can use this repo as a reference to mitigate the Privacy violation Heap Inspection issue [CWE-244](https://cwe.mitre.org/data/definitions/244.html)

## :warning: Important NOTE

___Never hardcode passwords or sensitive information in your code___. Use secret managers or vaults to securely store passwords and other sensitive data.

Even if you retrieve sensitive values from a secret manager or vault and assign them to a string variable, the issue remains. This is because sensitive data stored in a string cannot be cleared from memory immediately — it persists until garbage collection occurs.

For example, even if you initialize a StringBuffer or StringBuilder as shown below, an anonymous string object ("SecurePassword") is still created in heap memory and will remain there until garbage collection takes place:

```java
StringBuffer password = new StringBuffer("SecurePassword");
```

## Mitigation

:lock: Best Practices for Secure Coding

:one: Never store sensitive information in immutable strings :no_entry_sign:

:two: Understand how Garbage Collection (GC) works in your programming language 🧐

:three: Use Secret Managers or Vaults to store sensitive information :closed_lock_with_key:

:four: Use prebuilt Secure Strings to handle sensitive information

- Java: Use GuardedString (from Java's security libraries)
- C#: Use SecureString to handle sensitive data securely

5️⃣ When no prebuilt library exists, store passwords in char[] and overwrite the array after usage to ensure it doesn't linger in memory.

## Directory Structure
```
Privacy Violation - Heap Inspection
│   README.md
│
├───csharp
│       HeapInspectionCharArrayExample.cs
│       HeapInspectionSecureStringExample.cs
│
└───java
        HeapInspectionCharArrayExample.java
        HeapInspectionGuardedStringExample.java
```