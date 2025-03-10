export default function renderToggleSection(): HTMLElement {
    const div = document.createElement('div');
    div.classList.add('inline-group', 'toggle');
    div.innerHTML = `
        <button id="candidate" class="selected box">Candidato</button>
        <button id="business" class="box">Empresa</button>
    `;
    return div;
}