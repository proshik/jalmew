package main

import (
	"github.com/urfave/cli"
	"example_short/printer"
)

func main() {
	app := cli.NewApp()
	app.Name = "appName"

	printer.ToConsole("Hello world!")
}