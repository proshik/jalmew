package yandex

//Error response from Yandex dictionary
type ErrorResponse struct {
	Code    int    `json:"code"`
	Message string `json:"message"`
}

//Correct response from Yandex dictionary
type WordResponse struct {
	Head Head  `json:"head"`
	Def  []Def `json:"def"`
}

type Head struct{}

type Def struct {
	Text string `json:"text"`
	Pos  string `json:"pos"`
	Ts   string `json:"ts"`
	Tr   []Tr   `json:"tr"`
}

type Ex struct {
	Attr
	Tr
}

type Tr struct {
	Attr
	Syn  []Syn  `json:"syn"`
	Mean []Mean `json:"mean"`
	Ex   []Ex   `json:"ex"`
}

type Syn struct {
	Attr
}

type Mean struct {
	Attr
}

type Attr struct {
	Text string `json:"text"`
	Pos  string `json:"pos"`
	Gen  string `json:"gen"`
}

