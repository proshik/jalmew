package yandex

import (
	"encoding/json"
	"fmt"
	"net/http"
	"github.com/pkg/errors"
)

type Dictionary struct {
	token string
}

func NewDictionary(token string) *Dictionary {
	return &Dictionary{token}
}

//Throw standart error or Error
func (dict *Dictionary) Translate(text string, langFrom string, langTo string) (*WordResponse, *ExternalError) {

	url := fmt.Sprintf("https://dictionary.yandex.net/api/v1/dicservice.json/lookup?"+
		"lang=%s-%s&key=%s&text=%s", langFrom, langTo, dict.token, text)

	resp, err := http.Get(url)
	if err != nil {
		return nil, &ExternalError{err, 0}
	}

	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		d := json.NewDecoder(resp.Body)

		var erResp ErrorResponse
		err = d.Decode(&erResp)
		if err != nil {
			return nil, &ExternalError{err, 0}
		}
		return nil, &ExternalError{errors.New("Error from external system"), erResp.Code}
	}

	d := json.NewDecoder(resp.Body)

	var tr WordResponse
	err = d.Decode(&tr)
	if err != nil {

		return nil, err
	}

	return &tr, nil
}
