document.addEventListener("DOMContentLoaded", async () => {
    await loadHistory();
});

async function loadHistory() {
    try {
        const response = await fetch("http://localhost:8080/api/transactions/all");
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || "Failed to fetch transactions.");
        }
        const transactions = await response.json();

        const tableBody = document.querySelector("#historyTable tbody");
        tableBody.innerHTML = ""; // Изчистваме таблицата преди зареждане

        transactions.forEach(tx => {
            tableBody.innerHTML += `
                <tr>
                    <td>${tx.transactionId}</td>
                    <td>${new Date(tx.date).toLocaleString()}</td>
                    <td>${tx.sourceCurrency}</td>
                    <td>${tx.targetCurrency}</td>
                    <td>${tx.amount}</td>
                    <td>${tx.convertedAmount}</td>
                    <td>
                        <button onclick="deleteTransaction(${tx.transactionId})">🗑️ Delete</button>
                    </td>
                </tr>
            `;
        });
    } catch (error) {
        console.error("Error loading transactions:", error);
        alert("Неуспешно зареждане на историята. Проверете връзката с API.");
    }
}

async function deleteTransaction(transactionId) {
    if (!confirm("⚠️ Сигурни ли сте, че искате да изтриете тази транзакция?")) return;

    try {
        const response = await fetch(`http://localhost:8080/api/transactions/${transactionId}`, {
            method: "DELETE",
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || "Failed to delete transaction.");
        }

        alert("✅ Транзакцията беше изтрита успешно!");
        await loadHistory(); // Обновяване на таблицата
    } catch (error) {
        console.error("Error deleting transaction:", error);
        alert(`❌ Неуспешно изтриване: ${error.message}`);
    }
}
