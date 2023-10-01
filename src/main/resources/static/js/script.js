document.addEventListener('DOMContentLoaded',() => {
    let btnTabFullUrl = document.querySelector('.tab-btn-1');
    let btnTabCompactUrl = document.querySelector('.tab-btn-2');
    let fullUrlContent = document.querySelector('.full-url');
    let compactUrlContent = document.querySelector('.compact-url');
    let btn;
    btnTabCompactUrl.addEventListener('click', ()=> {
        fullUrlContent.classList.remove('active');
        compactUrlContent.classList.add('active');
    });
    btnTabFullUrl.addEventListener('click', ()=> {
        fullUrlContent.classList.add('active');
        compactUrlContent.classList.remove('active');
    })

    let dataTextFullUrl = document.querySelector('.text-full-url');
    let dataTextCompactUrl = document.querySelector('.text-compact-url');
    let btnFullUrl = document.querySelector('.btn-full-url');
    let btnCompactUrl = document.querySelector('.btn-compact-url');

    btnFullUrl.addEventListener('click',()=> {
        let uri = 'http://localhost:8080/api/v1/generate'
        let fullUrl = dataTextFullUrl.value;
        fetch(uri, {
            method: "POST",
            body: JSON.stringify({
                fullUrl
            }),
            headers:{
                "Content-Type":"application/json"
            }
        })
            .then(resp => resp.json())
            .then(resp => {
                if(resp.message){
                    alert(resp.message)
                }else{
                    showResult(resp);
                }
            })
    });

    btnCompactUrl.addEventListener('click',()=> {
        let uri = 'http://localhost:8080/api/v1/regenerate'
        let compactUrl = dataTextCompactUrl.value;
        fetch(uri, {
            method: "POST",
            body: JSON.stringify({
                compactUrl
            }),
            headers:{
                "Content-Type":"application/json"
            }
        })
            .then(resp => resp.json())
            .then(resp => {
                if(resp.message){
                    alert(resp.message)
                }else{
                    showResult(resp);
                }
            })
    });
    showResult = (resp) => {
        let lineFull = document.querySelector('.result-full');
        let lineCompact = document.querySelector('.result-compact');
        let htmlFullResult = `Full Url: ${resp.fullUrl}`;
        let htmlCompactResult = `Compact Url: ${resp.compactUrl}`;
        lineFull.innerHTML = htmlFullResult;
        lineCompact.innerHTML = htmlCompactResult;
        let fullUrl = dataTextFullUrl.value;
        if(!btn){
            createButton(fullUrl);
        }
    }
    const createButton = (fullUrl)=>  {
        btn = document.createElement("button");
        btn.textContent = "Dont like? Generate More"
        let myDiv = document.querySelector('.result');
        myDiv.appendChild(btn);
        btn.addEventListener('click', () => {
            let uri = 'http://localhost:8080/api/v1/regenerateCompact'
            let compactUrl = dataTextCompactUrl.value;
            fetch(uri, {
                method: "POST",
                body: JSON.stringify({
                    fullUrl
                }),
                headers:{
                    "Content-Type":"application/json"
                }
            })
                .then(resp => resp.json())
                .then(resp => {
                    if(resp.message){
                        alert(resp.message)
                    }else{
                        showResult(resp);
                    }
                });
        });
    }
})
