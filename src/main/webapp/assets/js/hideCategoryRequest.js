function hideCategory(indexID, categoryID, isChecked) {
    const params = new URLSearchParams();
    params.append("indexID", indexID);
    params.append("categoryID", categoryID);
    params.append("checked", isChecked);

    fetch('HideCategoryServlet', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: params.toString()
    })
        .then(response => {
            if (!response.ok) {
                console.error("Error updating category:");
            } else {
                location.reload();
            }
        })
        .catch(error => {
            console.error("Error updating category:", error);
        });
}
