SELECT
    v.id AS venda_id,
    v.data AS venda_data,
    v.empresa AS venda_empresa,
    v.total AS venda_total,
    v.taxa_desconto AS venda_desconto,
    v.situacao AS venda_situacao,
    c.nome AS nome_cliente,
    p.nome AS nome_produto,
    iv.valor AS valor_item,
    iv.quantidade AS quantidade_item
FROM venda v
         INNER JOIN cliente c ON v.id_cliente = c.id
         INNER JOIN item_de_venda iv ON v.id = iv.id_venda
         INNER JOIN produto p ON iv.id_produto = p.id
WHERE v.id = $P{venda_id}