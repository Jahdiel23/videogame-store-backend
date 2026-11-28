-- Run on videogame_store AFTER 01_catalogs.sql, with the app stopped and a backup available.
BEGIN;
DO $$
BEGIN
    IF current_database() <> 'videogame_store' THEN
        RAISE EXCEPTION 'Open this script on videogame_store, not the Market database.';
    END IF;
    IF to_regclass('public.videojuegos') IS NULL
       OR to_regclass('public.compras') IS NULL
       OR to_regclass('public.compras_productos') IS NULL THEN
        RAISE EXCEPTION 'Missing tables. Apply the catalog migration first.';
    END IF;
END $$;

ALTER TABLE public.compras ALTER COLUMN medio_pago TYPE varchar(20);
ALTER TABLE public.compras ALTER COLUMN estado TYPE varchar(20);
ALTER TABLE public.compras ALTER COLUMN comentario TYPE varchar(500);
ALTER TABLE public.compras_productos ALTER COLUMN total TYPE numeric(18,2) USING total::numeric(18,2);
ALTER TABLE public.compras_productos ADD COLUMN IF NOT EXISTS precio_unitario numeric(12,2);
-- Fill historical unit prices from saved totals when quantity is valid.
UPDATE public.compras_productos
SET precio_unitario = ROUND(total / cantidad, 2)
WHERE precio_unitario IS NULL AND cantidad > 0 AND total IS NOT NULL;
COMMIT;
