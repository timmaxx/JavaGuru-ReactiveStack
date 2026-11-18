docker compose up -d

docker compose down

------------------------------------------------------------------------------
Ситуация 1:
После некоторых входов/выходов Windows в/из гибернацию я пытался запустить:
docker compose up -d

Не запускался контейнер. Вывод был такой:
time="2026-11-18T13:52:04+03:00" level=warning msg="volume \"java_guru_reactive_java_postgresql_data_db_users\" already exists but was created for project \"docker\" (expected \"java_guru_reactive_java_postgresql_data_users\"). Use `external: true` to use an existing volume"
[+] Running 1/2
 ? Network java_guru_reactive_java_postgresql_data_users_default  Created                                                                                                                                                            0.1s
 - Container java_guru_reactive_java_postgresql_data_users        Starting                                                                                                                                                           0.4s
Error response from daemon: driver failed programming external connectivity on endpoint java_guru_reactive_java_postgresql_data_users (2282d0a3e15b8dfdc6e5427787b2605bf48204999411f07a121cd76604acbbd3): Bind for 0.0.0.0:5432 failed: port is already allocated

Т.е. Windows не давала прицепиться к своему порту (хостовому) 5432.
Причём, если поменять его, например, на 5439:5432, то старт был успешный.
Начал искать, где проблема:
1. Все запущенные контейнеры:
	docker ps
	не было других.
2. Ищу процесс по порту:
	(в powershell от админа)
	netstat -ano | findstr :5432
	нет процессов.
3. Проверяю, не зарезервирован ли порт 5432 системой Windows. После обновлений Windows или включения Hyper-V/WSL система часто блокирует случайные диапазоны портов (включая 5432).
	(в powershell от админа)
	netsh interface ipv4 show excludedportrange protocol=tcp
	увидел несколько диапазонов, но 5432 в них не попадает.
4. Полностью закрываю Docker Desktop и сбрасываю сетевой мост:
    правой кнопкой по значку кита в трее -> Quit Docker Desktop
	(в powershell от админа)
	wsl --shutdown
	потом (в том каталоге, где docker-compose.yml)
	docker compose up -d
	Помогло!
------------------------------------------------------------------------------

Ситуация 2:
В предыдущей ситуации была такая строка:
time="2026-11-18T13:52:04+03:00" level=warning msg="volume \"java_guru_reactive_java_postgresql_data_db_users\" already exists but was created for project \"docker\" (expected \"java_guru_reactive_java_postgresql_data_users\"). Use `external: true` to use an existing volume"

Устранение предупреждения о томе (warning).
Чтобы при следующих запусках docker compose up -d не появлялось сообщение о том,
что том java_guru_reactive_java_postgresql_data_db_users принадлежит другому проекту,
добавьте в ваш docker-compose.yml параметр "external: true":
volumes:
  db-data:
    name: "java_guru_reactive_java_postgresql_data_db_users"
    external: true

------------------------------------------------------------------------------
