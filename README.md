# CheckPoint 5 - Automação de Testes Funcionais (Login)

Automação em **Java + JUnit 5 + Selenium WebDriver** dos cenários funcionais
de login definidos no Plano de Testes do CheckPoint 4 (site de treino
[saucedemo.com](https://www.saucedemo.com/)).

## Cenários automatizados

| Caso | Status do plano (CP4) | O que valida |
|------|------------------------|---------------|
| CT1 | 200 / 302 | Login com credenciais válidas → redireciona para `inventory.html` |
| CT2 | 401 | Login com usuário e senha inválidos → mensagem de erro |
| CT3 | 403 | Login com usuário bloqueado (`locked_out_user`) → acesso negado |
| CT4 | 400 | Login com usuário e senha vazios → mensagem de validação |
| CT5 | 401 | Login com usuário correto e senha incorreta → mensagem de erro |
| CT6 | 400 | Login com campo de usuário vazio (senha preenchida) → mensagem de validação |

Os demais status marcados com ✓ no plano (408, 422, 429, 499, 500, 504)
dependem de comportamento de servidor/infraestrutura que não é possível
provocar de forma confiável apenas pela interface de um site público de
terceiros, então não foram incluídos na automação via UI
