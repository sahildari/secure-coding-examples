# Privacy Violation: Heap Inspection

__Privacy Violation: Heap Inspection__ is a source code security issue that occurs mostly in C#, Java, and Swift applications. Strings are immutable in these languages, meaning that if they are used to store sensitive information such as passwords, credit card numbers, secrets, or tokens, these values will remain in memory until the JVM garbage collector (in Java) or ARC (Automatic Reference Counting) (in Swift) removes them.

There is no guarantee as to when garbage collection will take place. In the event of an application crash, a memory dump might reveal sensitive data, making it accessible to anyone inspecting the heap before garbage collection occurs.

## :warning: Important NOTE

___Never hardcode passwords or sensitive information in your code___. Use secret managers or vaults to securely store passwords and other sensitive data.

Even if you retrieve sensitive values from a secret manager or vault and assign them to a string variable, the issue remains. This is because sensitive data stored in a string cannot be cleared from memory immediately — it persists until garbage collection occurs.

For example, even if you initialize a StringBuffer or StringBuilder as shown below, an anonymous string object ("SecurePassword") is still created in heap memory and will remain there until garbage collection takes place:

```java
StringBuffer password = new StringBuffer("SecurePassword");
```

## Mitigation

To mitigate the Privacy Violation: Heap Inspection issue in Java, C#, and Swift applications:

:white_check_mark: Use character arrays (char[]) instead of strings to store sensitive information. 

:white_check_mark: Manually clear arrays after use (e.g., overwriting with '\0').

:white_check_mark: Ensure cleanup happens in a finally block to guarantee execution.

By following these best practices, you reduce the risk of exposing sensitive data in memory dumps.