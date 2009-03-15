;; (my-get-classpath)
(set-variable 'jde-global-classpath
              (list "./bin/main"
                    "./bin/test"
                    "./lib/core.jar"
                    "./lib/video.jar"
                    "./lib/junit-4.5.jar"))
              

;;(setq jdibug-connect-host "localhost"
;;      jdibug-connect-port 5005)

(message "Loaded prj.el")
