export function showFeedbackError(input: HTMLInputElement | HTMLTextAreaElement , message: string): void {
    input.classList.add('invalid');
    if (input.nextElementSibling) {
        input.nextElementSibling.textContent = message;
        (input.nextElementSibling as HTMLElement).style.display = 'block';
    }
}

export function removeFeedbackError(input: HTMLInputElement | HTMLTextAreaElement): void {

    input.classList.remove('invalid');
    if (input.nextElementSibling) {
        input.nextElementSibling.textContent = '';
        (input.nextElementSibling as HTMLElement).style.display = 'none';
    }
}