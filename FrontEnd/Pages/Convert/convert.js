document.addEventListener("DOMContentLoaded", async () => {
    // 📌 Дефиниране на налични валути
    const currencies = ["USD", "EUR", "GBP", "BGN"]; 
    const fromCurrency = document.getElementById("fromCurrency");
    const toCurrency = document.getElementById("toCurrency");

    // 📌 Попълване на dropdown списъците с валути
    currencies.forEach(currency => {
        fromCurrency.innerHTML += `<option value="${currency}">${currency}</option>`;
        toCurrency.innerHTML += `<option value="${currency}">${currency}</option>`;
    });

    // 📌 Зареждане на историята на транзакциите
    //await loadHistory();
});

// 📌 Функция за получаване на обменния курс
async function getExchangeRate(fromCurrency, toCurrency) {
    try {
        const response = await fetch(`http://localhost:8080/api/exchange-rate?currentCurrency=${fromCurrency}&targetCurrency=${toCurrency}`);
        
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(`Грешка: ${errorData.message || "Обменният курс не може да бъде извлечен."}`);
        }

        const data = await response.json();
        return data.rate; // Връща обменния курс
    } catch (error) {
        console.error("❌ Грешка при извличане на обменния курс:", error);
        alert(error.message);
        return null;
    }
}

document.getElementById("convertBtn").addEventListener("click", async () => {
    const fromCurrency = document.getElementById("fromCurrency").value.trim().toUpperCase();
    const toCurrency = document.getElementById("toCurrency").value.trim().toUpperCase();
    const amount = parseFloat(document.getElementById("amount").value);

    if (!amount || amount <= 0 || !fromCurrency || !toCurrency) {
        alert("❌ Моля, попълнете всички полета с валидни данни!");
        return;
    }

    const requestBody = {
        amount: amount,
        sourceCurrency: fromCurrency,
        targetCurrency: toCurrency
    };

    console.log("Request body being sent:", JSON.stringify(requestBody));

    try {
        const response = await fetch("http://localhost:8080/api/convert", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(requestBody)
        });

        console.log("Response status:", response.status);

        if (!response.ok) {
            const errorData = await response.json();
            console.error("Error response body:", errorData);
            throw new Error(errorData.message || "Конверсията не може да бъде извършена.");
        }

        const data = await response.json();
        console.log("Response data:", data);

        // Показваме само Converted Amount и Transaction ID
        document.getElementById("result").innerText = `${data.convertedAmount} ${toCurrency}`;
        document.getElementById("transactionId").innerText = data.transactionId;
    } catch (error) {
        console.error("❌ Грешка при конвертиране:", error);
        alert(error.message);
    }
});












// 📌 Функция за зареждане на историята на транзакциите
async function loadHistory() {
    try {
        const response = await fetch("http://localhost:8080/api/transactions/all");

        if (!response.ok) {
            throw new Error("Грешка при зареждане на историята на транзакциите.");
        }

        const transactions = await response.json();
        const tableBody = document.querySelector("#historyTable tbody");
        tableBody.innerHTML = ""; // Изчистваме преди ново зареждане

        transactions.forEach(tx => {
            tableBody.innerHTML += `
                <tr>
                    <td>${new Date(tx.date).toLocaleString()}</td>
                    <td>${tx.sourceCurrency}</td>
                    <td>${tx.targetCurrency}</td>
                    <td>${tx.amount}</td>
                    <td>${tx.convertedAmount}</td>
                    <td><button onclick="deleteTransaction(${tx.transactionId})">🗑️</button></td>
                </tr>
            `;
        });
    } catch (error) {
        console.error("❌ Грешка при зареждане на историята:", error);
        alert("Неуспешно зареждане на историята. Проверете връзката с API.");
    }
}

// 📌 Функция за изтриване на транзакция по ID
async function deleteTransaction(transactionId) {
    if (!confirm("⚠️ Сигурни ли сте, че искате да изтриете тази транзакция?")) return;

    try {
        const response = await fetch(`http://localhost:8080/api/transactions/${transactionId}`, { method: "DELETE" });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(`Грешка: ${errorData.message || "Неуспешно изтриване на транзакцията."}`);
        }

        alert("✅ Транзакцията беше изтрита успешно!");
        await loadHistory();
    } catch (error) {
        console.error("❌ Грешка при изтриване на транзакцията:", error);
        alert(error.message);
    }
}
