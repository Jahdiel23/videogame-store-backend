-- READ-ONLY. Replace purchase ID 1 with the real ID returned by POST /purchases.
SELECT c.id_compra, c.id_cliente, c.fecha, c.medio_pago, c.estado,
       COUNT(d.id_producto) AS detalles, SUM(d.total) AS total_compra
FROM public.compras c
JOIN public.compras_productos d ON d.id_compra = c.id_compra
WHERE c.id_compra = 1
GROUP BY c.id_compra, c.id_cliente, c.fecha, c.medio_pago, c.estado;

SELECT d.id_compra, d.id_producto AS id_videojuego, v.titulo,
       d.cantidad, d.precio_unitario, d.total, d.estado, d.fecha
FROM public.compras_productos d
JOIN public.videojuegos v ON v.id_videojuego = d.id_producto
WHERE d.id_compra = 1
ORDER BY d.id_producto;

SELECT id_videojuego, titulo, precio, stock FROM public.videojuegos ORDER BY id_videojuego;

-- Before and after an invalid request, both counts and the game stock must remain unchanged.
SELECT COUNT(*) AS compras FROM public.compras;
SELECT COUNT(*) AS detalles FROM public.compras_productos;
