document.addEventListener("DOMContentLoaded", function () {

    const chatbotButton =
        document.getElementById("chatbot-button");

    const chatbotWindow =
        document.getElementById("chatbot-window");

    const chatbotClose =
        document.getElementById("chatbot-close");

    const chatbotInput =
        document.getElementById("chatbot-input");

    const chatbotSend =
        document.getElementById("chatbot-send");

    const chatbotMessages =
        document.getElementById("chatbot-messages");



    chatbotButton.addEventListener("click", function () {

        chatbotWindow.style.display = "flex";

        chatbotInput.focus();

    });


    chatbotClose.addEventListener("click", function () {

        chatbotWindow.style.display = "none";

    });


    async function sendMessage() {

        const message =
            chatbotInput.value.trim();


        // Don't send empty message

        if (!message) {
            return;
        }


        // Show user message

        addUserMessage(message);


        // Clear input

        chatbotInput.value = "";


        // Disable send button

        chatbotSend.disabled = true;


        // Show typing indicator

        showTypingIndicator();


        try {

            const response = await fetch(
                "/api/user/ai/chat",
                {
                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify({
                        request: message
                    })
                }
            );


            // Check HTTP response

            if (!response.ok) {

                throw new Error(
                    "AI API returned status: "
                    + response.status
                );
            }


            // Read backend response

            const aiResponse =
                await response.text();


            // Remove typing indicator

            removeTypingIndicator();


            // Show AI response

            addAiMessage(aiResponse);


        } catch (error) {

            console.error(
                "Chatbot error:",
                error
            );


            removeTypingIndicator();


            addAiMessage(
                "Sorry, I couldn't process your request. Please try again."
            );


        } finally {


            chatbotSend.disabled = false;

            chatbotInput.focus();

        }

    }

    function addUserMessage(message) {

        const messageElement =
            document.createElement("div");


        messageElement.classList.add(
            "message",
            "user-message"
        );


        const content =
            document.createElement("div");

        content.classList.add(
            "message-content"
        );


        content.textContent = message;


        messageElement.appendChild(content);

        chatbotMessages.appendChild(
            messageElement
        );


        scrollToBottom();

    }


    function addAiMessage(message) {

        const messageElement =
            document.createElement("div");


        messageElement.classList.add(
            "message",
            "ai-message"
        );


        const avatar =
            document.createElement("div");

        avatar.classList.add(
            "message-avatar"
        );

        avatar.textContent = "🤖";


        const content =
            document.createElement("div");

        content.classList.add(
            "message-content"
        );


        content.textContent = message;


        messageElement.appendChild(avatar);

        messageElement.appendChild(content);


        chatbotMessages.appendChild(
            messageElement
        );


        scrollToBottom();

    }

    function showTypingIndicator() {

        const typing =
            document.createElement("div");


        typing.id =
            "typing-indicator";


        typing.classList.add(
            "message",
            "ai-message"
        );


        typing.innerHTML = `
            <div class="message-avatar">
                🤖
            </div>

            <div class="message-content">
                Typing...
            </div>
        `;


        chatbotMessages.appendChild(
            typing
        );


        scrollToBottom();

    }


    function removeTypingIndicator() {

        const typing =
            document.getElementById(
                "typing-indicator"
            );


        if (typing) {

            typing.remove();

        }

    }


    function scrollToBottom() {

        chatbotMessages.scrollTop =
            chatbotMessages.scrollHeight;

    }

    chatbotSend.addEventListener(
        "click",
        sendMessage
    );

    chatbotInput.addEventListener(
        "keydown",
        function (event) {

            if (event.key === "Enter") {

                event.preventDefault();

                sendMessage();

            }

        }
    );

});