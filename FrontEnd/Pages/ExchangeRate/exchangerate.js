document.getElementById("getRateBtn").addEventListener("click", async () => {
    const fromCurrency = document.getElementById("fromCurrency").value.trim();
    const toCurrency = document.getElementById("toCurrency").value.trim();

    if (!fromCurrency || !toCurrency) {
        alert("❌ Please enter valid currency codes for both fields!");
        return;
    }

    console.log("From Currency:", fromCurrency);
    console.log("To Currency:", toCurrency);

    try {
        
        const response = await fetch(`http://localhost:8080/api/exchange-rate?sourceCurrency=${fromCurrency}&targetCurrency=${toCurrency}`);
        console.log("Response status:", response.status);

        if (!response.ok) {
            const errorData = await response.json();
            console.error("Error response body:", errorData);
            throw new Error(errorData.message || "Failed to fetch exchange rate.");
        }

        const rate = await response.json();
        console.log("Exchange rate received:", rate);

        if (rate !== undefined) {
            document.getElementById("result").innerText = `Exchange Rate: ${rate}`;
        } else {
            alert("Exchange rate not found in the response.");
        }
    } catch (error) {
        console.error("❌ Error fetching exchange rate:", error);
        alert(error.message);
    }
});
