Notas para a correção:<br>
1 - App feito em jetpack compose + XML, motivo: diferenças entre experiencias dos desenvolvedores e criação de experiencia para os desenvolvedores em projeto que envolvam as duas tecnologias;<br>
2 - Dividimos as tarefas (organização da equipe) conforme o quadro:<br>
    https://www.notion.so/Board-de-tarefas-1ade91a1f7cf80268545ef5bed2c38ee<br>
3 - Tentamos concentrar toda a comunicação no grupo 01 do discord, além de não estar presente o óbvio: o que programamos em conjunto em chamada no meet, mas registramos lá também!.<br>
4 - Plus: implementamos inteligencia artificial: Batalha inteligente. O usuário clica nas espadinhas e pode selecionar dois pokemons para batalharem, a resposta vem numa outra tela, em texto, gerada pela API da OpenAI.<br>
5 - O nome da chave de api no local.properties é: "API_KEY".<br>
6 - Resumo da divisão de tarefas: Bruna e Mariana: responsáveis pelas UI feitas em XML e integração das informações com os dados vindos das API.<br>
    Alan: responsável pela arquitetura, organização do código, Splash screen e jetpack compose;<br>
    Philipe: Chamadas de API (para ambos os endpoints dos pokemons), criação dos DTOs e suporte na integração das informações às UI XML.<br>
7 - Os estados de erro e loading geraram algumas confusões, vamos a ela:<br>
    -Para a dela que foi detalhada em XML, não sabemos como aplicar o padrão observer, segue com erro e loading bem simples por enquanto.<br>
    -Para as telas de compose, os estados de erro e loading são mais fáceis de manusear, portanto estão melhores.<br>
8 - Ultima atualização: 17/03/2025 às 09:40:00 GMT-3<br>
