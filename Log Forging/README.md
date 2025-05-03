# Log Forging

__Log Forging__ is a security vulnerability that occurs when unvalidated user input is written to the log files, allowing attackers to manipulate log entries or inject malicious content. Attackers can create misleading log entries, compromising the integrity of the logs and pushing the SOC team or the blue team into a rabit hole while creating false log entries. Common methods of log injection include __CRLF Injection__ It is also known as __Log Injection__

You can use this repo as reference to mitigate the Log Forging/Log Injection vulnerability [CWE-117](https://cwe.mitre.org/data/definitions/117.html).

## :warning: Important NOTE

Never hardcode sensitive information in the logs(like passwords, tokens, user details, PII, PFI, PHI, etc.) it can result into other vulnerabilities, Privacy Violation [CWE-359](https://cwe.mitre.org/data/definitions/359.html), Trust Boundary Violation [CWE-501](https://cwe.mitre.org/data/definitions/501.html), Sensitive information exposure in the logs[CWE-532](https://cwe.mitre.org/data/definitions/532.html).

## Mitigation

:lock: Best Practices for Secure Coding in the Logs.

:one: Input Sanitization to escape the CRLF characters and other dangerous characters.

:two: Use Parameterized Logging

:three: Restrict Log File Access

:four: Centralized Logging and Monitoring