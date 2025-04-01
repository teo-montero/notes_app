document.getElementById("dumpFileBtn").addEventListener("click", async function () {
    const fileInput = document.getElementById("fileInput");
    const file = fileInput.files[0];
    const contentBox = document.querySelector("textarea[name='content']");
    const fileError = document.getElementById("fileError");
    fileError.style.display = "none";

    if (!file) {
        fileError.textContent = "Please select a file.";
        fileError.style.display = "block";
        return;
    }

    const fileName = file.name.toLowerCase();

    try {
        let text = "";

        if (fileName.endsWith(".txt")) {
            text = await file.text();
        } else if (fileName.endsWith(".pdf")) {
            text = await readPdfFile(file);
        } else if (fileName.endsWith(".docx")) {
            text = await readDocxFile(file);
        } else {
            fileError.textContent = "Unsupported file type. Only .txt, .pdf, or .docx allowed.";
            fileError.style.display = "block";
            return;
        }

        contentBox.value += "\n" + text;

        // Close modal
        const modal = bootstrap.Modal.getInstance(document.getElementById("dumpModal"));
        modal.hide();
    } catch (error) {
        fileError.textContent = "Error reading file: " + error.message;
        fileError.style.display = "block";
    }
});

async function readPdfFile(file) {
    const reader = new FileReader();
    return new Promise((resolve, reject) => {
        reader.onload = async function () {
            const typedArray = new Uint8Array(reader.result);
            const pdf = await pdfjsLib.getDocument(typedArray).promise;
            let fullText = "";
            for (let i = 1; i <= pdf.numPages; i++) {
                const page = await pdf.getPage(i);
                const content = await page.getTextContent();
                fullText += content.items.map(item => item.str).join(" ") + "\n";
            }
            resolve(fullText);
        };
        reader.onerror = () => reject(reader.error);
        reader.readAsArrayBuffer(file);
    });
}

async function readDocxFile(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = function (event) {
            mammoth.extractRawText({ arrayBuffer: event.target.result })
            .then(result => resolve(result.value))
            .catch(err => reject(err));
        };
        reader.onerror = () => reject(reader.error);
        reader.readAsArrayBuffer(file);
    });
}