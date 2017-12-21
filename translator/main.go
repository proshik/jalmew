package translator

import (
	"net/http"
	"github.com/go-chi/chi"
	"log"
)

func main() {
	r := chi.NewRouter()
	r.Route("/translate", func(r chi.Router) {
		r.Get("/word", func(w http.ResponseWriter, r *http.Request) {

		})
		r.Post("/text", func(writer http.ResponseWriter, r *http.Request) {

		})
	})

	log.Println("Api is waiting for requests...")

	http.ListenAndServe(":8080", r)

}
