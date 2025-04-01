<script>
  function updateCategory(noteID, categoryID, isChecked) {
    const params = new URLSearchParams();
    params.append("noteID", noteID);
    params.append("categoryID", categoryID);
    params.append("checked", isChecked);

    fetch('UpdateCategoryServlet', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: params.toString() // Properly encodes parameters
    })
            .then(response => {
              if (!response.ok) {
                alert("🚨 Failed to update category.");
              } else {
                console.log("✅ Category updated successfully!");
              }
            })
            .catch(error => {
              console.error("🚨 Error updating category:", error);
            });
  }
</script>