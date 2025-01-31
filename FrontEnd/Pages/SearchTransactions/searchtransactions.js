
document.getElementById("searchByIdBtn").addEventListener("click", async () => {
    const transactionId = document.getElementById("transactionId").value;

    if (!transactionId) {
        alert("Please enter a transaction ID.");
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/api/${transactionId}`);

        if (!response.ok) {
            throw new Error("Transaction not found.");
        }

        const transaction = await response.json();

        
        const resultsTable = document.getElementById("resultsTable").querySelector("tbody");
        resultsTable.innerHTML = "";

        
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${transaction.transactionId}</td>
            <td>${transaction.date}</td>
            <td>${transaction.sourceCurrency}</td>
            <td>${transaction.targetCurrency}</td>
            <td>${transaction.amount}</td>
            <td>${transaction.convertedAmount}</td>
        `;
        resultsTable.appendChild(row);
    } catch (error) {
        console.error("Error fetching transaction by ID:", error);
        alert(error.message);
    }
});


document.getElementById("searchByCriteriaBtn").addEventListener("click", async () => {
    const startDate = document.getElementById("startDate").value;
    const endDate = document.getElementById("endDate").value;
    const sourceCurrency = document.getElementById("sourceCurrency").value.trim();
    const targetCurrency = document.getElementById("targetCurrency").value.trim();

    
    const criteria = {};
    if (startDate) criteria.startDate = startDate;
    if (endDate) criteria.endDate = endDate;
    if (sourceCurrency) criteria.sourceCurrency = sourceCurrency.toUpperCase();
    if (targetCurrency) criteria.targetCurrency = targetCurrency.toUpperCase();

    
    if (Object.keys(criteria).length === 0) {
        alert("❌ Please enter at least one search criterion.");
        return;
    }

    try {
        
        const response = await fetch("http://localhost:8080/api/transactions/search", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(criteria)
        });

        
        if (!response.ok) {
            throw new Error("❌ No transactions found.");
        }

        const transactions = await response.json();

        
        const resultsTable = document.getElementById("resultsTable").querySelector("tbody");
        resultsTable.innerHTML = "";

        
        if (transactions.length === 0) {
            alert("❌ No transactions match the criteria.");
            return;
        }

        
        transactions.forEach(transaction => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${transaction.transactionId || "N/A"}</td>
                <td>${transaction.date || "N/A"}</td>
                <td>${transaction.sourceCurrency || "N/A"}</td>
                <td>${transaction.targetCurrency || "N/A"}</td>
                <td>${transaction.amount || "N/A"}</td>
                <td>${transaction.convertedAmount || "N/A"}</td>
            `;
            resultsTable.appendChild(row);
        });
    } catch (error) {
        console.error("❌ Error fetching transactions by criteria:", error);
        alert(error.message || "An error occurred while searching for transactions.");
    }
});
