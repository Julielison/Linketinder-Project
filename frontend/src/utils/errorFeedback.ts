export function showMessageError(name: string, message: string): void {
    const form = document.getElementById("form");
    const input = form?.querySelector(`input[name="${name}"]`) as HTMLInputElement;

    input.classList.add('invalid');
    if (input.nextElementSibling) {
        input.nextElementSibling.textContent = message;
        (input.nextElementSibling as HTMLElement).style.display = 'block';
    }
}