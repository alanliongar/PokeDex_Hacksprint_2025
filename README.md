Notas para a correção:
1 - App feito em jetpack compose + XML, motivo: diferenças entre experiencias dos desenvolvedores e criação de experiencia para os desenvolvedores em projeto que envolvam as duas tecnologias;
2 - Dividimos as tarefas (organização da equipe) conforme o quadro:
    https://www.notion.so/Board-de-tarefas-1ade91a1f7cf80268545ef5bed2c38ee
3 - Tentamos concentrar toda a comunicação no grupo 01 do discord, além de não estar presente o óbvio: o que programamos em conjunto em chamada no meet, mas registramos lá também!.
4 - Plus: implementamos inteligencia artificial: Batalha inteligente. O usuário clica nas espadinhas e pode selecionar dois pokemons para batalharem, a resposta vem numa outra tela, em texto, gerada pela API da OpenAI.
5 - O nome da chave de api no local.properties é: "API_KEY".
6 - Resumo da divisão de tarefas: Bruna e Mariana: responsáveis pelas UI feitas em XML e integração das informações com os dados vindos das API.
    Alan: responsável pela arquitetura, organização do código, Splash screen e jetpack compose;
    Philipe: Chamadas de API (para ambos os endpoints dos pokemons), criação dos DTOs e suporte na integração das informações às UI XML.
7 - Os estados de erro e loading geraram algumas confusões, vamos a ela:
    7.1 - Da tela de lista para o erro, criamos uma intent para navegar pro erro. Ao voltar, o usuário volta pra lista (isso ok)
    7.2 - Da tela de detalhes para a tela de erro: quando o usuário tá sem internet, e vai pra tela de detalhes, colocamos uma intent pra navegar pra terceira tela de erros. aí ao voltar, o usuário se depara com a tela de detalhes vazia.
    7.3