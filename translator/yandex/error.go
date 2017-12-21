package yandex

type Code int

const (
	OK                       = 200
	KEY_INVALID              = 401
	KEY_BLOCKED              = 402
	DAILY_REQ_LIMIT_EXCEEDED = 403
	TEXT_TOO_LONG            = 413
	LANG_NOT_SUPPORTED       = 501
)

type ExternalError struct {
	error
	Code int
}
