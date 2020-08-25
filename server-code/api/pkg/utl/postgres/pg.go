package postgres

import (
	"database/sql"
	"fmt"
)

// DBConnect returns a DB object for to query things.
func DBConnect(connURL string) (*sql.DB, error) {
	db, err := sql.Open("postgres", "postgres://ashwinbhavnani@localhost/home_led_settings?sslmode=disable")
	if err != nil {
		fmt.Println("nur")
		return nil, err
	}

	if err := db.Ping(); err != nil {
		fmt.Println("didn't work bruh")
		return nil, err
	}
	return db, nil
}
