
DROP TABLE IF EXISTS public.game_state;
CREATE TABLE public.game_state (
    id serial NOT NULL PRIMARY KEY,
    state_name text NOT NULL,
    current_map text NOT NULL,
    saved_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    player_id integer NOT NULL
);

DROP TABLE IF EXISTS public.player CASCADE;
CREATE TABLE public.player (
    id serial NOT NULL PRIMARY KEY,
    player_name text NOT NULL,
    hp integer NOT NULL,
    x integer NOT NULL,
    y integer NOT NULL
);

DROP TABLE IF EXISTS public.inventory;
CREATE TABLE public.inventory (
	id serial NOT NULL PRIMARY KEY,
	items text NOT NULL,
	inventory_map text NOT NULL,
	owner_id integer NOT NULL
);

ALTER TABLE public.game_state
    ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES public.player(id);
ALTER TABLE public.inventory
    ADD CONSTRAINT fk_owner_id FOREIGN KEY (owner_id) REFERENCES public.player(id);
