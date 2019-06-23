<a href="https://github.com/BulatMukhutdinov/currency-exchange/blob/master/CurrencyExchanger.apk?raw=true" download>Click to download the apk</a>
# Task description
The goal is to create simple application for displaying currency exchange rates. The App should contains two screens:

1. Currencies list
   - List of all available currencies for exchange, e.g "USD", "EUR", etc...
   - Some UI element which enables users to search or filter the list.
   - When an element is pressed/selected, the second screen (part 2) should be opened
   - The list of currencies can be fetched at: `https://api.exchangeratesapi.io/latest`
     - If this API is offline/blocked, you can try instead `https://raw.githubusercontent.com/wireapp/sample-data/master/currencies/list.json`
	 
To clarify, the screen should resemble the following (feel free to adjust margins, tweak/add labels and buttons, etc.):
```
+---------------------+
|SEARCH: ____________ |
|---------------------|
|AUD                  |
|BGN                  |
|BRL                  |
|CAD                  |
|CHF                  |
|CNY                  |
|CZK                  |
|DKK                  |
|GBP                  |
+---------------------+
```
2. Exchange rates
   - Use `ViewPager` and `TabLayout` for this screen. Each page should contain a list of all exchange rates for a given currency. For example, if the user picks "USD", the list will contain "USD/CAD : 1.349329", "USD/CHF 1.0090892889" and so on.
   - Each page should have a "reload content" feature.
   - The exchange rate for a specific currency can be fetched at `https://api.exchangeratesapi.io/latest?base=USD` where USD is the base currency you want to compare.
     - If this API is offline/blocked, you can try instead `https://raw.githubusercontent.com/wireapp/sample-data/master/currencies/USD/rates.json` where `USD` is the base currency you want to compare.

To clarify, the screen should resemble the following (feel free to adjust margins, tweak/add labels and buttons, etc.):
```
+---------------------+
|<| AUD | BGN | BRL | |
|---------------------|
|AUD/BGN  3.14159     |
|AUD/BRL  3.14159     |
|AUD/CAD  3.14159     |
|AUD/CHF  3.14159     |
|AUD/CNY  3.14159     |
|AUD/CZK  3.14159     |
|AUD/DKK  3.14159     |
|AUD/GBP  3.14159     |
+---------------------+
```

The app should run on Android 7 or higher, and be written in Kotlin. 

Everything else is up to you, implement it however you want. Feel free to add what you think is needed. 

### Quality requirements

- Feel free to use any libraries that you want to.
- Use an architecture appropriate for big mobile app.
- Write a few tests. We do not expect full test coverage. Test only the most critical parts of app.
- Please do not forget about screen rotations. These should be handled properly.
