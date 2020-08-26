package colors

import (
	"database/sql"
	"fmt"
	"log"
	"net/http"

	"github.com/go-chi/chi"
	"github.com/go-chi/chi/middleware"
	"github.com/go-chi/render"
)

// Resource returns a subrouter for a specific endopoint.
type Resource struct {
	DB *sql.DB
} //colors endpoint

// Routes returns specific API routes
func (rs Resource) Routes() chi.Router {
	r := chi.NewRouter()
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(render.SetContentType(render.ContentTypeJSON))

	r.Get("/", rs.List) // GET /todos - read a list of todos
	//r.Post("/", rs.Create) // POST /todos - create a new todo and persist it
	//r.Put("/", rs.Delete)
	return r
}

// List should return all the colors in the DB.
func (rs Resource) List(w http.ResponseWriter, r *http.Request) {
	if err := rs.DB.Ping(); err != nil {
		fmt.Println("didn't work bruh")
	}
	rows, err := rs.DB.Query("SELECT name, red, green, blue, white from colors")
	if err != nil {
		//should return a http error code.
		log.Fatal(err)
	}

	var (
		name  string
		red   int
		green int
		blue  int
		white int
	)

	for rows.Next() {
		err := rows.Scan(&name, &red, &green, &blue, &white)
		if err != nil {
			log.Fatal(err)
		}
		fmt.Println(name, red, green, blue, white)
		w.Write([]byte(name + fmt.Sprint(red) + fmt.Sprint(green) + fmt.Sprint(blue) + fmt.Sprint(white)))
	}

	// (w << 24) | (r << 16) | (g << 8) | b) = colors.rgbw

	fmt.Println("color get")
	w.Write([]byte("aaa list of stuff.."))
}

// the following does json. how to adapt my code? read a query into a struct, read that into json?!
/*
type Todo struct {
	Slug  string `json:"slug"`
	Title string `json:"title"`
	Body  string `json:"body"`
}


func (config *Config) GetATodo(w http.ResponseWriter, r *http.Request) {
	todoID := chi.URLParam(r, "todoID")
	todos := Todo{
		Slug:  todoID,
		Title: "Hello world",
		Body:  "Heloo world from planet earth",
	}
	render.JSON(w, r, todos) // A chi router helper for serializing and returning json
}
*/
