export default function renderTab(tabName: string, renderFunction:  ): HTMLElement {
    const header = document.querySelector('header') as HTMLElement;

    header.innerHTML = `
    <nav>
        <button class="tab" id="${tabName}-btn">${tabName}</button>
    </nav>
    `;

    const tabButton = document.getElementById(`${tabName}-btn`) as HTMLButtonElement;

    tabButton?.addEventListener('click', () => {
        renderProfile(tabName);
    });

    return header;
}

// Função chamada quando a aba é clicada
function handleTabClick(tabName: string) {
    console.log(`A aba ${tabName} foi clicada.`);
    // Adicione a lógica para o que acontece quando a aba for clicada.
    // Por exemplo, exibir conteúdo ou navegar.
}
