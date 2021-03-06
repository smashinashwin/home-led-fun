   /*
   static uint32_t   Color(uint8_t r, uint8_t g, uint8_t b, uint8_t w) {
     return ((uint32_t)w << 24) | ((uint32_t)r << 16) | ((uint32_t)g <<  8) | b;
   }
*/
CREATE TABLE IF NOT EXISTS colors (
	rgbw 	bigint PRIMARY KEY, /*follow the adafruit standard for r,g,b,w 32-bit integers */
	name 	varchar UNIQUE NOT NULL,
	hex		varchar, 
	red		smallint,
	green	smallint,
	blue	smallint,
	white	smallint
);

CREATE TABLE IF NOT EXISTS palettes (
	name varchar PRIMARY KEY,
	colors integer[]
);

CREATE TABLE IF NOT EXISTS pattern_settings (
	name 				varchar PRIMARY KEY,
	pattern 			varchar, /* defined on arduino only for now */
	pattern_settings	jsonb,
	palette				varchar,
	FOREIGN KEY (palette) 
		REFERENCES palettes (name)
);

CREATE TABLE IF NOT EXISTS schedules (
	name 				varchar PRIMARY KEY,
	days_of_week 		smallint[],
	pattern				varchar,
	pattern_settings	varchar,
	color				integer,
	palette				varchar,
	start_time			time,
	end_time			time,
	FOREIGN KEY (pattern_settings)
		REFERENCES pattern_settings (name),
	FOREIGN KEY (color)
		REFERENCES colors (rgbw),
	FOREIGN KEY (palette)
		REFERENCES palettes (name)
);