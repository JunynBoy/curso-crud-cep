# CepSystem

Aplicação Java Web com JSF, PrimeFaces, JPA/Hibernate e MySQL para cadastro e consulta de unidades de saúde por faixa de CEP.

## Funcionalidades

- Cadastro, edição e remoção de unidades de saúde.
- Validação de CNES único.
- Validação de sobreposição entre intervalos de CEP.
- Busca por nome do estabelecimento, CNES, CEP inicial ou CEP final.
- Geração de relatório em PDF com JasperReports.

## Stack

- Java 17
- JSF 2.3
- PrimeFaces 10
- CDI/Weld
- JPA/Hibernate
- MySQL
- JasperReports

## Configuração do banco

Por padrão a aplicação usa:

- URL: `jdbc:mysql://localhost:3306/rangdatabase?useSSL=false&serverTimezone=UTC`
- Usuário: `root`
- Senha: vazia

As credenciais podem ser sobrescritas por variáveis de ambiente:

- `DB_DRIVER`
- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`
- `DB_SCHEMA_STRATEGY`
- `DB_SHOW_SQL`

Também é possível usar propriedades de sistema equivalentes:

- `database.driver`
- `database.url`
- `database.user`
- `database.password`
- `database.schema`
- `database.show_sql`

## Build

```bash
mvn clean package
```

O arquivo `.war` é gerado em `target/cepsystem.war`.
