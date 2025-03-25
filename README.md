[![Java CI with Gradle](https://github.com/mbi88/date-handler/actions/workflows/gradle.yml/badge.svg)](https://github.com/mbi88/date-handler/actions/workflows/gradle.yml)
[![codecov](https://codecov.io/gh/mbi88/date-handler/branch/master/graph/badge.svg)](https://codecov.io/gh/mbi88/date-handler)
[![Latest Version](https://img.shields.io/github/v/tag/mbi88/date-handler?label=version)](https://github.com/mbi88/date-handler/releases)
[![jitpack](https://jitpack.io/v/mbi88/date-handler.svg)](https://jitpack.io/#mbi88/date-handler)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)


---

# date-handler

Lightweight and test-friendly Java library for date/time manipulation and parsing using intuitive formula syntax.  
Built on top of [Joda-Time](https://www.joda.org/joda-time/).

---

## ✅ Features

- Get current date and time
- Add or subtract human-readable time offsets (`1y2M3d4h5m6s`)
- Calculate days between two dates
- Get specific parts of a date (year, month, day, etc.)
- Detect and format `yyyy-MM-dd` and `yyyy-MM-dd'T'HH:mm:ss`
- Built-in support for time zones via `DateTimeZone`
- Fully covered with tests, ready for automation

---

## 📦 Installation

<details>
<summary>Gradle (Kotlin DSL)</summary>

`implementation("com.mbi:date-handler:1.0")`

</details>

<details>
<summary>Gradle (Groovy DSL)</summary>

`implementation 'com.mbi:date-handler:1.0'`

</details>

---

## 📚 Formula syntax

Formula is a string composed of pairs: number + unit. Example:

- `1y` – one year
- `2M` – two months
- `3d` – three days
- `4h` – four hours
- `5m` – five minutes
- `6s` – six seconds

You can combine them like this:  
`1y2M3d4h5m6s` → adds full period to date

---

## 🚀 Example usage

```java
import com.mbi.DateHandler;
import org.joda.time.DateTimeZone;
import org.testng.annotations.Test;

import java.util.TimeZone;

public class DateHandlerTest {

    @Test
    public void testName() {
        DateHandler dateHandler = new DateHandler(DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/Kiev")));

        // Current time is 2018-03-20T11:34:18. Method will return: 2022-05-20T13:34:18
        System.out.println(dateHandler.plus("4y2M2h"));

        // Returns 365
        System.out.println(dateHandler.daysBetweenDates("2017-01-01", "2018-01-01"));

        // Returns Tuesday
        System.out.println(dateHandler.getDayOfWeek("2019-01-01"));
    }
}
```

---

## ✅ API Examples

- Add to now:  
  `date.plus("3d5h")` → adds 3 days and 5 hours to current datetime

- Subtract from now:  
  `date.minus("1M")` → subtracts 1 month

- Add to specific date:  
  `date.plus("2024-01-01", "2d")` → `"2024-01-03"`

- Subtract from datetime:  
  `date.minus("2024-01-04T01:00:00", "1d2h")` → `"2024-01-02T23:00:00"`

- Get parts of date:  
  `date.getYear("2023-05-01T13:15:30")` → `2023`  
  `date.getMonth()` → current month

- Get weekday:  
  `date.getDayOfWeek("2024-03-25")` → `"Monday"`

- Days between dates:  
  `date.daysBetweenDates("2023-01-01", "2023-01-10")` → `9`

---

## ❌ Invalid input examples

```java
date.plus("abc");          // Invalid format: must start with digit and end with letter  
date.plus("1y2M3");        // Invalid format  
date.plus("2d5mx");        // Unrecognized field: d5mx
```

---

## 🧪 Test-friendly

This library is built for automation use:

- Easy assertions on string/number output
- Compatible with TestNG or JUnit
- All logic is validated and edge cases covered

---

## 🕓 Time zone handling

`DateHandler` supports any `DateTimeZone`. Default is `UTC`. Example:

```java
DateHandler handler = new DateHandler(DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/Kiev")));
```

---

## See also

- [Joda-Time GitHub](https://github.com/JodaOrg/joda-time)
- [Joda-Time Homepage](https://www.joda.org/joda-time/)
- [Java DateTime Format Guide](https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html)

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

---

🕒 Built on Joda-Time · 💡 Formula-based manipulation · ✅ Ready for testing
