/*Api structure:
/v1/api/ashleds/schedue 	CRUD
/v1/api/ashleds/color 		CRUD
/v1/api/ashleds/pattern	CRUD
/v1/api/ashleds/palette 	CRUD (can only create palettes out of existing colors?
Or allow new colors and just push them to the colors table if they don’t exist)
*/

package main

import (
	"log"
	"net/http"

	"ashwin.com/leds/lightserver/pkg/utl/postgres"

	_ "github.com/lib/pq"

	"ashwin.com/leds/lightserver/pkg/resources/colors"
	"github.com/go-chi/chi"
	"github.com/go-chi/chi/middleware"
	"github.com/go-chi/render"
)

func main() {
	/*
		router.Route("/v1", func(r chi.Router) {
			r.Get("/api/leds/color", getColors)
		})
	*/

	db, err := postgres.DBConnect("postgres://ashwinbhavnani@localhost/home_led_settings?sslmode=disable")
	if err != nil {
		log.Fatal(err)
		panic(err)
	}

	router := chi.NewRouter()
	router.Use(middleware.RequestID)
	router.Use(middleware.RealIP)
	router.Use(middleware.Logger)
	router.Use(middleware.Recoverer)
	router.Use(render.SetContentType(render.ContentTypeJSON))

	router.Mount("/colors", colors.Resource{db}.Routes())

	http.ListenAndServe(":3333", router)
}
